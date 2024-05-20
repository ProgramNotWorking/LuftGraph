package org.example

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.JFreeChart
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import javax.swing.JFrame
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

object Graph {

    // dataset for graph
    private val dataset = XYSeriesCollection()
    private val mainSeries = XYSeries("t")
    private val functionLine = XYSeries("Function line")
    private val uLine = XYSeries("u(t)")
    private val constLine = XYSeries("psi(t)")

    // values
    private var h: Int = 0
    private var u0: Int = 0
    private var t0: Int = 0
    private const val t: Int = 5 // temp
    private var trident: Int = 0

    // value for graph length
    private const val T = 40.0

    fun drawGraph() {
        askForData()
        initGraphWithValues()
        println("Производная psi: ${findTridentDerivative(t.toDouble())}")
    }

    private fun initGraphWithValues() {
        for (t in 0..(T * 10).toInt() step 1) {
            val point = (t / 10).toDouble()
            if (isFunctionIncreasing(
                    function = { x: Double -> 3 * sin(x) },
                    intervalEnd = t.toDouble()
                )
            ) {
                constLine.add(point, max(u0.toDouble(), bOfT(t.toDouble())))
            } else {
                constLine.add(point, min(u0.toDouble(), bOfT(t.toDouble()) + h))
            }
            uLine.add(point, uOfT(t) { x: Double -> x - 2 })
            functionLine.add(point, findTridentDerivative(t.toDouble()))
            mainSeries.add(point, 0)
        }

        dataset.addSeries(functionLine)
        dataset.addSeries(constLine)
        dataset.addSeries(mainSeries)

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

    private fun askForData() {
        println("Введите h: ")
        h = readLine()?.toInt() ?: 1
        println("Введите u0: ")
        u0 = readLine()?.toInt() ?: 3
        println("Введите t0: ")
        t0 = readLine()?.toInt() ?: 0
        println("Введите psi: ")
        trident = readLine()?.toInt() ?: 1
    }

    // Fun for derivative (just print it at end)
    private fun findTridentDerivative(t: Double): Double =
        if (trident > bOfT(t) && trident < (bOfT(t) + h)) {
            0.0
        } else if (trident.toDouble() == bOfT(t)) {
            max(0.0, bOfT(t)).toDouble()
        } else if (trident.toDouble() == bOfT(t) + h) {
            min(0.0, bOfT(t))
        } else {
            10.0
        }

    // Sigma of t value i guess
    private fun bOfT(t: Double): Double = t - 2

    private fun uOfT(t: Int, b: (Double) -> Double) =
        if (isFunctionIncreasing({ x: Double -> x - 2 }, 0.0, 10.0)) {
            min(u0.toDouble(), b(t.toDouble()))
        } else {
            max(u0.toDouble(), b(t.toDouble() + h))
        }

    private fun isFunctionIncreasing(
        function: (Double) -> Double,
        intervalStart: Double = 0.0,
        intervalEnd: Double
    ): Boolean {
        if (intervalEnd <= intervalStart) return false // Интервал пустой или некорректный

        var lastValue = function(intervalStart)
        for (i in intervalStart.toInt() until intervalEnd.toInt()) {
            val currentValue = function(i.toDouble())
            if (currentValue >= lastValue) return false // Значение не увеличивается
            lastValue = currentValue
        }

        return true // Все значения увеличиваются
    }

}