package com.example.testapp

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.util.Stack
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private var currentInput: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        display = findViewById(R.id.display)
        val buttonIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide, R.id.btnDot,
            R.id.btnLeftParen, R.id.btnRightParen, R.id.btnSqrt
        )
        val buttonValues = mapOf(
            R.id.btn0 to "0", R.id.btn1 to "1", R.id.btn2 to "2", R.id.btn3 to "3", R.id.btn4 to "4",
            R.id.btn5 to "5", R.id.btn6 to "6", R.id.btn7 to "7", R.id.btn8 to "8", R.id.btn9 to "9",
            R.id.btnPlus to "+", R.id.btnMinus to "-", R.id.btnMultiply to "*", R.id.btnDivide to "/",
            R.id.btnDot to ".",
            R.id.btnLeftParen to "(", R.id.btnRightParen to ")", R.id.btnSqrt to "√"
        )

        for (id in buttonIds) {
            findViewById<MaterialButton>(id).setOnClickListener {
                val value = buttonValues[id] ?: ""
                if (value == "√") {
                    currentInput += "sqrt("
                } else {
                    currentInput += value
                }
                display.text = currentInput
            }
        }

        findViewById<MaterialButton>(R.id.btnClear).setOnClickListener {
            currentInput = ""
            display.text = "0"
        }

        findViewById<MaterialButton>(R.id.btnEquals).setOnClickListener {
            try {
                val result = evaluateExpression(currentInput)
                display.text = result
                currentInput = result
            } catch (e: Exception) {
                display.text = "Error"
                currentInput = ""
            }
        }
    }

    // Simple expression evaluator supporting +, -, *, /, sqrt, parentheses
    private fun evaluateExpression(expr: String): String {
        val replaced = expr
            .replace("sqrt(", "√(") // for easier parsing
        val tokens = tokenize(replaced)
        val rpn = toRPN(tokens)
        val result = evalRPN(rpn)
        return if (result % 1.0 == 0.0) result.toInt().toString() else result.toString()
    }

    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        while (i < expr.length) {
            when (val c = expr[i]) {
                in '0'..'9', '.' -> {
                    var num = ""
                    while (i < expr.length && (expr[i].isDigit() || expr[i] == '.')) {
                        num += expr[i]
                        i++
                    }
                    tokens.add(num)
                    continue
                }
                '+', '-', '*', '/', '(', ')' -> tokens.add(c.toString())
                '√' -> tokens.add("√")
                else -> {}
            }
            i++
        }
        return tokens
    }

    private fun toRPN(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val ops = Stack<String>()
        val precedence = mapOf(
            "+" to 1, "-" to 1, "*" to 2, "/" to 2, "√" to 4
        )
        val rightAssoc = setOf("√")
        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> output.add(token)
                token == "√" -> ops.push(token)
                token in precedence -> {
                    while (ops.isNotEmpty() && ops.peek() != "(" &&
                        (precedence[ops.peek()]!! > precedence[token]!! ||
                                (precedence[ops.peek()] == precedence[token] && token !in rightAssoc))
                    ) {
                        output.add(ops.pop())
                    }
                    ops.push(token)
                }
                token == "(" -> ops.push(token)
                token == ")" -> {
                    while (ops.isNotEmpty() && ops.peek() != "(") {
                        output.add(ops.pop())
                    }
                    if (ops.isNotEmpty() && ops.peek() == "(") ops.pop()
                }
            }
        }
        while (ops.isNotEmpty()) output.add(ops.pop())
        return output
    }

    private fun evalRPN(rpn: List<String>): Double {
        val stack = Stack<Double>()
        for (token in rpn) {
            when {
                token.toDoubleOrNull() != null -> stack.push(token.toDouble())
                token == "+" -> stack.push(stack.pop() + stack.pop())
                token == "-" -> {
                    val b = stack.pop(); val a = stack.pop(); stack.push(a - b)
                }
                token == "*" -> stack.push(stack.pop() * stack.pop())
                token == "/" -> {
                    val b = stack.pop(); val a = stack.pop(); stack.push(a / b)
                }
                token == "√" -> stack.push(sqrt(stack.pop()))
            }
        }
        return stack.pop()
    }
}