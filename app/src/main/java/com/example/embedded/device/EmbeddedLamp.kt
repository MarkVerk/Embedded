package com.example.embedded.device

class EmbeddedLamp : Device() {
    var isOn: Boolean = false
    var brightness: Int = 0
    var effect: Int = 0
    var effectSpeed: Float = 0.0f
    var palette: Int = 0
    var effects = ArrayList<String>()
    var palettes = ArrayList<String>()
}