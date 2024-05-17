package org.example
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import javax.swing.JFrame
import kotlin.math.max
import kotlin.math.sin

fun main() {
    // IDK about that rn
    val dataset = XYSeriesCollection()
    val wSeries = XYSeries("w(t)")
    val bSeries = XYSeries("b")
    val aSeries = XYSeries("a")
    val xSeries = XYSeries("x(t)")
    val mainSeries = XYSeries("t")

    // Sigma i guess
    fun b(t: Int) = kotlin.math.abs(kotlin.math.abs(t) - 2)

    // New values
    val h = 1
    val u0 = 3
    val t0 = 0
    val t = 0
    val trident = 1

    // Fun for derivative (just print it at end)
    fun findTridentDerivative(): Int =
        if (trident > b(t) && trident < (b(t) + h)) { 0 }
        else if (trident == b(t)) { max(0, b(t)) }
        else if (trident == b(t) + h) { max(0, b(t)) }
        else { -1 }

    // Do something like this but with new values
    val T = 40.0
    println("Введите значение верхнего предела: ")
    val b: Double = readLine()?.toDouble() ?: 2.996
    println("Введите значение нижнего предела: ")
    val a: Double = readLine()?.toDouble() ?: -1.0
    println("Введите значение K(большое число): ")
    val K: Double = readLine()?.toDouble() ?: 1000000.0
    println("Введите значение w: ")
    val w: Double = readLine()?.toDouble() ?: 1.0

    // x(t) function example
    fun x(t: Double) = 3 * sin(t)

    fun calculateW(t: Double, operation: (Double) -> Double): Double {
        return K * max(operation(t) - b, 0.0) * (1 - w) - K * max(a - operation(t), 0.0)
    }

    for (t in 0..(T * 10).toInt() step 1) {
        val w0 = if (x((t / 10).toDouble()) >= 0) {
            1.0
        } else {
            0.0
        }
        mainSeries.add((t / 10).toDouble(), 0)
        wSeries.add((t / 10).toDouble(), w0)
        bSeries.add((t / 10).toDouble(), b)
        aSeries.add((t / 10).toDouble(), a)
        xSeries.add((t / 10).toDouble(), x((t / 10).toDouble()))
    }

    val tempW = calculateW(w.toDouble()) { x(it) }
    // println("Значение производной w: $tempW")

    dataset.addSeries(mainSeries)
    dataset.addSeries(wSeries)
    dataset.addSeries(bSeries)
    dataset.addSeries(aSeries)
    dataset.addSeries(xSeries)

    val chart: JFreeChart = ChartFactory.createXYLineChart(
        "График",
        "t",
        "y",
        dataset
    )

    val frame = JFrame("График")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.add(ChartPanel(chart))
    frame.pack()
    frame.setLocationRelativeTo(null)
    frame.isVisible = true
}