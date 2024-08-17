package com.github.skozlov.algorithms.sort

import com.github.skozlov.Test
import com.github.skozlov.commons.collection.{Slice, WritableSlice}

class SortTest extends Test {
  private val cases: Seq[Seq[(Int, Int)]] = {
    Seq(
      Seq.empty[Int],
      Seq(1),
      Seq(1, 2, 3),
      Seq(1, 3, 2),
      Seq(2, 1, 3),
      Seq(2, 3, 1),
      Seq(3, 1, 2),
      Seq(3, 2, 1),
      Seq(1, 3, 2, 3, 1),
    ) map { _.zipWithIndex }
  }

  private implicit val ordering: Ordering[(Int, Int)] =
    Ordering.by[(Int, Int), Int](_._1)

  private val commonExpectedResults: Seq[Seq[Int]] = cases map {
    _.map { _._1 }.sorted
  }

  private val stableSortExpectedResults: Seq[Seq[(Int, Int)]] = cases map {
    _.sorted(Ordering.by[(Int, Int), Int](_._1).orElseBy(_._2))
  }

  private def checkResults(
      results: Seq[Seq[(Int, Int)]],
      stableSort: Boolean,
  ): Unit = {
    if (stableSort) {
      results shouldBe stableSortExpectedResults
    } else {
      (results map { _ map { _._1 } }) shouldBe commonExpectedResults
    }
  }

  private def testInPlaceSort(sort: InPlaceSort[(Int, Int)]): Unit = {
    val arrays: Seq[Array[(Int, Int)]] = cases map { _.toArray }
    val results: Seq[Seq[(Int, Int)]] = for (array <- arrays) yield {
      sort.sortInPlace(WritableSlice(array)())
      array.toSeq
    }
    checkResults(results, sort.isInstanceOf[StableSort])
  }

  private def testFunctionalSort(sort: FunctionalSort[(Int, Int)]): Unit = {
    val inputsAndOutputsAfterSort: Seq[(Array[(Int, Int)], Array[(Int, Int)])] =
      for {
        _case: Seq[(Int, Int)] <- cases
        input: Array[(Int, Int)] = _case.toArray
        output: Array[(Int, Int)] = sort.sortFunctionally(Slice(input)())
      } yield (input, output)
    checkResults(
      inputsAndOutputsAfterSort map { _._2.toSeq },
      sort.isInstanceOf[StableSort],
    )
    (inputsAndOutputsAfterSort map { _._1.toSeq }) shouldBe cases
  }

  test("insertion sort") {
    testInPlaceSort(InsertionSort())
    testFunctionalSort(InsertionSort())
  }
}
