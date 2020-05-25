package com.creyc.test.models

data class Cells(
    var mines: Int,
    var flags: Boolean,
    var opened: Boolean,
    var near: Int
)