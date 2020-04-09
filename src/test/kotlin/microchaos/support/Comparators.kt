package microchaos.support

import java.util.*


fun closeToComparator(epsilon: Double = 1e-6): Comparator<Double> {
  return Comparator<Double> { a, b -> if (Math.abs(a - b) < epsilon) 0 else -1 }
}