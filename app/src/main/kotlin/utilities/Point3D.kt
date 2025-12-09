package ghoti.maedjyuk.app.utilities

import kotlin.math.pow
import kotlin.math.sqrt

data class Point3D(
    val x: Int,
    val y: Int,
    val z: Int
)

fun Point3D.distanceTo(other: Point3D): Double =
    sqrt(
        (x - other.x).toDouble().pow(2) +
                (y - other.y).toDouble().pow(2) +
                (z - other.z).toDouble().pow(2)
    )
