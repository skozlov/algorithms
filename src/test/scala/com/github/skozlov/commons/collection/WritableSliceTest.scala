package com.github.skozlov.commons.collection

import com.github.skozlov.Test

class WritableSliceTest extends Test {
  test("constructor") {
    val underlying = Array(1, 2, 3, 4, 5)
    WritableSlice(underlying)(from = 1, until = 4).toSeq shouldBe Seq(2, 3, 4)
    WritableSlice(underlying)(until = 2).toSeq shouldBe Seq(1, 2)
    WritableSlice(underlying)(from = 3).toSeq shouldBe Seq(4, 5)
    WritableSlice(underlying)(from = 1, until = 1).toSeq shouldBe Seq()
    the[IllegalArgumentException] thrownBy WritableSlice(underlying)(
      from = -1,
      until = 4,
    ) should have message "requirement failed: Negative `from`: -1"
    the[IllegalArgumentException] thrownBy WritableSlice(underlying)(
      from = 2,
      until = 1,
    ) should have message "requirement failed: `from` (2) is greater than `until` (1)"
    the[IllegalArgumentException] thrownBy WritableSlice(underlying)(
      from = 3,
      until = 6,
    ) should have message "requirement failed: `until` (6) is greater than underlying length (5)"
  }

  test("changing underlying") {
    val underlying = Array(1, 2, 3, 4, 5)
    val slice = WritableSlice(underlying)(from = 1, until = 4)
    slice.toSeq shouldBe Seq(2, 3, 4)
    for (i <- underlying.indices) {
      underlying(i) = underlying(i) * 10
    }
    slice.toSeq shouldBe Seq(20, 30, 40)
  }

  test("length") {
    val underlying = Array(1, 2, 3, 4, 5)
    WritableSlice(underlying)(from = 1, until = 4).length shouldBe 3
    WritableSlice(underlying)(from = 1, until = 2).length shouldBe 1
    WritableSlice(underlying)(from = 1, until = 1).length shouldBe 0
  }

  test("apply(i: Int)") {
    val slice =
      WritableSlice(underlying = Array(1, 2, 3, 4))(from = 1, until = 3)
    slice(0) shouldBe 2
    slice(1) shouldBe 3
    the[IndexOutOfBoundsException] thrownBy slice(
      2
    ) should have message "2 is out of bounds (min 0, max 1)"
    the[IndexOutOfBoundsException] thrownBy slice(
      -1
    ) should have message "-1 is out of bounds (min 0, max 1)"
  }

  test("update(idx: Int, elem: A)") {
    val underlying = Array(1, 2, 3, 4)
    val slice = WritableSlice(underlying)(from = 1, until = 3)
    slice(0) = 20
    slice(1) = 30
    the[IndexOutOfBoundsException] thrownBy {
      slice(2) = 40
    } should have message "2 is out of bounds (min 0, max 1)"
    the[IndexOutOfBoundsException] thrownBy {
      slice(-1) = 10
    } should have message "-1 is out of bounds (min 0, max 1)"
    underlying.toSeq shouldBe Seq(1, 20, 30, 4)
  }

  test("slice(from: Int, until: Int)") {
    val underlying = Array(1, 2, 3, 4, 5, 6, 7)
    val slice = WritableSlice(underlying)(from = 1, until = 6)
    slice.slice(from = 1, until = 4).toSeq shouldBe Seq(3, 4, 5)
    slice.slice(from = 0, until = 5).toSeq shouldBe Seq(2, 3, 4, 5, 6)
    slice.slice(from = 1, until = 1).toSeq shouldBe Seq()
    val subSlice: WritableSlice[Int] =
      slice.slice(from = 0, until = 0) // checking sub-slice type
    the[IllegalArgumentException] thrownBy slice.slice(
      from = -1,
      until = 5,
    ) should have message "requirement failed: Negative `from`: -1"
    the[IllegalArgumentException] thrownBy slice.slice(
      from = 1,
      until = 0,
    ) should have message "requirement failed: `from` (1) is greater than `until` (0)"
    the[IllegalArgumentException] thrownBy slice.slice(
      from = 0,
      until = 6,
    ) should have message "requirement failed: `until` (6) is greater than underlying length (5)"
  }

  test("take(n: Int)") {
    val slice =
      WritableSlice(underlying = Array(1, 2, 3, 4))(from = 1, until = 3)
    (slice take 0).toSeq shouldBe Seq()
    (slice take -1).toSeq shouldBe Seq()
    (slice take 1).toSeq shouldBe Seq(2)
    (slice take 2).toSeq shouldBe Seq(2, 3)
    (slice take 3).toSeq shouldBe Seq(2, 3)
  }

  test("drop(n: Int)") {
    val slice =
      WritableSlice(underlying = Array(1, 2, 3, 4))(from = 1, until = 3)
    (slice drop 0).toSeq shouldBe Seq(2, 3)
    (slice drop -1).toSeq shouldBe Seq(2, 3)
    (slice drop 1).toSeq shouldBe Seq(3)
    (slice drop 2).toSeq shouldBe Seq()
    (slice drop 3).toSeq shouldBe Seq()
  }

  test("takeRight(n: Int)") {
    val slice =
      WritableSlice(underlying = Array(1, 2, 3, 4))(from = 1, until = 3)
    (slice takeRight 0).toSeq shouldBe Seq()
    (slice takeRight -1).toSeq shouldBe Seq()
    (slice takeRight 1).toSeq shouldBe Seq(3)
    (slice takeRight 2).toSeq shouldBe Seq(2, 3)
    (slice takeRight 3).toSeq shouldBe Seq(2, 3)
  }

  test("dropRight(n: Int)") {
    val slice =
      WritableSlice(underlying = Array(1, 2, 3, 4))(from = 1, until = 3)
    (slice dropRight 0).toSeq shouldBe Seq(2, 3)
    (slice dropRight -1).toSeq shouldBe Seq(2, 3)
    (slice dropRight 1).toSeq shouldBe Seq(2)
    (slice dropRight 2).toSeq shouldBe Seq()
    (slice dropRight 3).toSeq shouldBe Seq()
  }

  test("tail") {
    val underlying = Array(1, 2, 3, 4, 5)
    the[UnsupportedOperationException] thrownBy {
      WritableSlice(underlying)(from = 1, until = 1).tail
    } should have message "Empty Slice doesn't have .tail"
    WritableSlice(underlying)(from = 1, until = 2).tail.toSeq shouldBe Seq()
    WritableSlice(underlying)(from = 1, until = 4).tail.toSeq shouldBe Seq(3, 4)
  }

  test("init") {
    val underlying = Array(1, 2, 3, 4, 5)
    the[UnsupportedOperationException] thrownBy {
      WritableSlice(underlying)(from = 1, until = 1).init
    } should have message "Empty Slice doesn't have .init"
    WritableSlice(underlying)(from = 1, until = 2).init.toSeq shouldBe Seq()
    WritableSlice(underlying)(from = 1, until = 4).init.toSeq shouldBe Seq(2, 3)
  }

  test("tails") {
    val underlying = Array(1, 2, 3, 4)
    (WritableSlice(underlying)(from = 1, until = 1).tails map { _.toSeq }).toSeq
      .shouldBe(Seq(Seq()))
    (WritableSlice(underlying)(from = 1, until = 3).tails map { _.toSeq }).toSeq
      .shouldBe(Seq(Seq(2, 3), Seq(3), Seq()))
  }

  test("inits") {
    val underlying = Array(1, 2, 3, 4)
    (WritableSlice(underlying)(from = 1, until = 1).inits map { _.toSeq }).toSeq
      .shouldBe(Seq(Seq()))
    (WritableSlice(underlying)(from = 1, until = 3).inits map { _.toSeq }).toSeq
      .shouldBe(Seq(Seq(2, 3), Seq(2), Seq()))
  }

  test("sliding") {
    val slice = WritableSlice(Array(1, 2, 3, 4, 5, 6, 7))(from = 1, until = 6)
    slice.toSeq shouldBe Seq(2, 3, 4, 5, 6)
    slice.slice(from = 0, until = 0).sliding(size = 1).toSeq shouldBe Seq()
    (slice.sliding(size = 1) map { _.toSeq }).toSeq shouldBe Seq(
      Seq(2),
      Seq(3),
      Seq(4),
      Seq(5),
      Seq(6),
    )
    (slice.sliding(size = 2) map { _.toSeq }).toSeq shouldBe Seq(
      Seq(2, 3),
      Seq(3, 4),
      Seq(4, 5),
      Seq(5, 6),
      Seq(6),
    )
    (slice.sliding(size = 2, step = 2) map { _.toSeq }).toSeq shouldBe Seq(
      Seq(2, 3),
      Seq(4, 5),
      Seq(6),
    )
    (slice.sliding(size = 2, step = 3) map { _.toSeq }).toSeq shouldBe Seq(
      Seq(2, 3),
      Seq(5, 6),
    )
  }

  test("grouped") {
    val slice = WritableSlice(Array(1, 2, 3, 4, 5, 6, 7))(from = 1, until = 6)
    slice.toSeq shouldBe Seq(2, 3, 4, 5, 6)
    slice.slice(from = 0, until = 0).grouped(size = 1).toSeq shouldBe Seq()
    (slice.grouped(size = 1) map { _.toSeq }).toSeq shouldBe Seq(
      Seq(2),
      Seq(3),
      Seq(4),
      Seq(5),
      Seq(6),
    )
    (slice.grouped(size = 2) map { _.toSeq }).toSeq shouldBe Seq(
      Seq(2, 3),
      Seq(4, 5),
      Seq(6),
    )
  }
}
