package com.example.ilham_accelerometer // Nama package aplikasi

import android.graphics.Color // Import Warna grafis yang dibutuhkan

import android.hardware.Sensor // Import komponen sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

import androidx.appcompat.app.AppCompatActivity // Import Tampilan Pada aplikasi
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity(), SensorEventListener {

    /* Membuat Instance dari Class Sensor Manager dengan memanggil
    getSystemService() dan meneruskan argumen SENSOR_SERVICE */

    private lateinit var sensorManager: SensorManager
    private lateinit var square: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Membuat fungsi agar smartphone tetap dalam mode terang
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        square = findViewById(R.id.tv_square)

        setUpSensorStuff()
    }

    private fun setUpSensorStuff() {
        // Lanjutan dari proses pembuatan sensor manager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Menentukkan Sensor pendengar
        // memanggil metode getDefaultSensor() dan menggunakan konstanta TYPE_ACCELEROMETER
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

        // Memeriksa sensor yang telah didaftarkan
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            //Log.d("Main", "onSensorChanged: sides ${event.values[0]} front/back ${event.values[1]} ")

            // Sides = Tilting phone left(10) and right(-10)
            val sides = event.values[0]

            // Up/Down = Tilting phone up(10), flat (0), upside-down(-10)
            val upDown = event.values[1]

            square.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }

            /* Membuat fungsi agar persegi bisa berubah warna jika persegi dalam kondisi benar-benar rata
            * Jika persegi dalam kondisi miring maka akan berwarna BIRU
            * dan jika persegi dalam kondisi benar-benar rata maka akan berwarna abu-abu*/
            val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.LTGRAY else Color.BLUE
            square.setBackgroundColor(color)

            square.text = "atas/bawah ${upDown.toInt()}\nkiri/kanan ${sides.toInt()}"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}