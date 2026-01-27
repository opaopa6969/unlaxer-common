package org.unlaxer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.unlaxer.Source.SourceKind;

/**
 * 座標系（root/subSource/nested subSource）と lineNumber / positionInLine が
 * 常に「root座標」に基づいて一貫していることを検証するテスト。
 *
 * 仕様根拠：
 * - Cursor#lineNumber は positionInRoot を使う
 * - Cursor#positionInLine も positionInRoot を使う
 * - subSource は offsetFromRoot を合成して root座標を維持する
 */
public class SourceCoordinateSystemLineNumberTest {

  private static CursorRange pointAtRoot(Source root, int rootPos) {
    // StartInclusiveCursorImpl/EndExclusiveCursorImpl は CodePointAccessor.cursorRange() と同じ作り方に合わせる
    return root.cursorRange(
        new CodePointIndex(rootPos),
        new CodePointIndex(rootPos),
        SourceKind.root,
        root.positionResolver()
    );
  }

  private static CursorRange pointAtSub(Source sub, int subPos) {
    CodePointOffset offsetFromRoot = sub.offsetFromRoot(); // subのroot基準オフセット
    CodePointIndex rootPos = offsetFromRoot.toCodePointIndex().newWithAdd(subPos);

    // Cursor内部positionはroot座標、position()は(position - offsetFromRoot)でsub座標になる :contentReference[oaicite:3]{index=3}
    return CursorRange.of(
        rootPos,
        rootPos,
        offsetFromRoot,
        SourceKind.subSource,
        sub.positionResolver()
    );
  }
  
  @Test
  public void root_cursor_lineNumber_and_positionInLine_LF() {
    // 0: A 1: B 2:\n 3: C 4: D 5:\n 6: E
    Source root = StringSource.createRootSource("AB\nCD\nE");

    // 'A' (0): line 0, col 0
    {
      CursorRange p = pointAtRoot(root, 0);
      assertEquals(new LineNumber(0), p.startIndexInclusive().lineNumber());
      assertEquals(new CodePointIndexInLine(0), p.startIndexInclusive().positionInLine());
      assertEquals(new CodePointIndex(0), p.startIndexInclusive().positionInRoot());
      assertEquals(new CodePointIndex(0), p.startIndexInclusive().position()); // rootは position == positionInSub
    }

    // 'C' (3): line 1, col 0
    {
      CursorRange p = pointAtRoot(root, 3);
      assertEquals(new LineNumber(1), p.startIndexInclusive().lineNumber());
      assertEquals(new CodePointIndexInLine(0), p.startIndexInclusive().positionInLine());
      assertEquals(new CodePointIndex(3), p.startIndexInclusive().positionInRoot());
      assertEquals(new CodePointIndex(3), p.startIndexInclusive().position());
    }

    // 'D' (4): line 1, col 1
    {
      CursorRange p = pointAtRoot(root, 4);
      assertEquals(new LineNumber(1), p.startIndexInclusive().lineNumber());
      assertEquals(new CodePointIndexInLine(1), p.startIndexInclusive().positionInLine());
    }

    // 'E' (6): line 2, col 0
    {
      CursorRange p = pointAtRoot(root, 6);
      assertEquals(new LineNumber(2), p.startIndexInclusive().lineNumber());
      assertEquals(new CodePointIndexInLine(0), p.startIndexInclusive().positionInLine());
    }
  }

  @Test
  public void subSource_cursor_uses_root_coordinates_for_lineNumber_and_inLine() {
    Source root = StringSource.createRootSource("AAA\nBBB\nCCC");

    // "BBB" は root上で [4,7)
    Source sub = root.subSource(new CodePointIndex(4), new CodePointIndex(7));

    // subの先頭 (subPos=0) は rootPos=4 → line 1, col 0
    {
      CursorRange p = pointAtSub(sub, 0);
      assertEquals(new LineNumber(1), p.startIndexInclusive().lineNumber());
      assertEquals(new CodePointIndexInLine(0), p.startIndexInclusive().positionInLine());

      // sub座標は0、root座標は4
      assertEquals(new CodePointIndex(0), p.startIndexInclusive().position());
      assertEquals(new CodePointIndex(4), p.startIndexInclusive().positionInRoot());
    }

    // subの2文字目 (subPos=1) は rootPos=5 → line 1, col 1
    {
      CursorRange p = pointAtSub(sub, 1);
      assertEquals(new LineNumber(1), p.startIndexInclusive().lineNumber());
      assertEquals(new CodePointIndexInLine(1), p.startIndexInclusive().positionInLine());
      assertEquals(new CodePointIndex(1), p.startIndexInclusive().position());
      assertEquals(new CodePointIndex(5), p.startIndexInclusive().positionInRoot());
    }
  }

  @Test
  public void nested_subSource_composes_offsets_and_keeps_root_lineNumber() {
    Source root = StringSource.createRootSource("AAA\nBBB\nCCC");

    // root[4,7) = "BBB"
    Source sub1 = root.subSource(new CodePointIndex(4), new CodePointIndex(7));
    // sub1[1,2) = "B"（root上は index 5）
    Source sub2 = sub1.subSource(new CodePointIndex(1), new CodePointIndex(2));

    // sub2先頭(subPos=0) は rootPos=5 → line 1 col 1
    CursorRange p = pointAtSub(sub2, 0);
    assertEquals(new LineNumber(1), p.startIndexInclusive().lineNumber());
    assertEquals(new CodePointIndexInLine(1), p.startIndexInclusive().positionInLine());

    assertEquals(new CodePointIndex(0), p.startIndexInclusive().position());
    assertEquals(new CodePointIndex(5), p.startIndexInclusive().positionInRoot());
  }

  @Test
  public void lineNumber_handles_CRLF_and_CR_and_LF() {
    // code point index:
    // 0:'A' 1:'\r' 2:'\n' 3:'B' 4:'\r' 5:'C' 6:'\n' 7:'D'
    Source root = StringSource.createRootSource("A\r\nB\rC\nD");

    // 行頭は：0(A行), 3(B行), 5(C行), 7(D行)
    assertEquals(new LineNumber(0), root.lineNumberFrom(new CodePointIndex(0)));
    assertEquals(new LineNumber(1), root.lineNumberFrom(new CodePointIndex(3)));
    assertEquals(new LineNumber(2), root.lineNumberFrom(new CodePointIndex(5)));
    assertEquals(new LineNumber(3), root.lineNumberFrom(new CodePointIndex(7)));

    // 位置ベースでも確認（cursorが positionInRoot を使う前提）
    CursorRange p = pointAtRoot(root, 7);
    assertEquals(new LineNumber(3), p.startIndexInclusive().lineNumber());
    assertEquals(new CodePointIndexInLine(0), p.startIndexInclusive().positionInLine());
  }

  @Test
  public void surrogate_pair_does_not_break_codePoint_based_coordinates() {
    // 😀 は surrogate pair だが codePointIndex 的には 1個
    // "X😀Y\nZ"
    // codePoint index:
    // 0:'X' 1:'😀' 2:'Y' 3:'\n' 4:'Z'
    Source root = StringSource.createRootSource("X😀Y\nZ");

    // 😀 (1) は line 0, col 1
    CursorRange p = pointAtRoot(root, 1);
    assertEquals(new LineNumber(0), p.startIndexInclusive().lineNumber());
    assertEquals(new CodePointIndexInLine(1), p.startIndexInclusive().positionInLine());
  }
}
