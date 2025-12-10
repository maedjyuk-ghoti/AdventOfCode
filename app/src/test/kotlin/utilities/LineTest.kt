package utilities

import ghoti.maedjyuk.app.utilities.Line
import ghoti.maedjyuk.app.utilities.Point2D
import ghoti.maedjyuk.app.utilities.intersects
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LineTest {
    @Test
    fun `parallel, non-touching lines don't intersect`() {
        val line1 = Line(Point2D(0, 0), Point2D(1, 0))
        val line2 = Line(Point2D(0, 1), Point2D(1, 1))

        assertFalse { line1.intersects(line2) }
    }

    @Test
    fun `parallel overlapping lines do intersect`() {
        val line1 = Line(Point2D(0, 0), Point2D(2, 0))
        val line2 = Line(Point2D(1, 0), Point2D(3, 0))

        assertTrue { line1.intersects(line2) }
    }

    @Test
    fun `parallel overlapping lines don't intersect when line1 is a subset of line2`() {
        val line1 = Line(Point2D(1, 0), Point2D(2, 0))
        val line2 = Line(Point2D(0, 0), Point2D(3, 0))

        assertFalse { line1.intersects(line2) }
    }

    @Test
    fun `parallel overlapping lines do intersect when line2 is a subset of line1`() {
        val line1 = Line(Point2D(0, 0), Point2D(3, 0))
        val line2 = Line(Point2D(1, 0), Point2D(2, 0))

        assertTrue { line1.intersects(line2) }
    }

    @Test
    fun `non-parallel lines do intersect at vertex`() {
        val line1 = Line(Point2D(0, 0), Point2D(1, 0))
        val line2 = Line(Point2D(0, 0), Point2D(0, 1))

        assertTrue { line1.intersects(line2) }
    }

    @Test
    fun `non-parallel lines do intersect in middle`() {
        val line1 = Line(Point2D(0, 1), Point2D(2, 1))
        val line2 = Line(Point2D(1, 0), Point2D(1, 2))

        assertTrue { line1.intersects(line2) }
    }
}