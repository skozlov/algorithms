package com.github.skozlov.commons

import scala.collection.mutable

package object collection {
  type SomeMutableIndexedSeqOps[A] = mutable.IndexedSeqOps[A, AnyConstr, _]
}
