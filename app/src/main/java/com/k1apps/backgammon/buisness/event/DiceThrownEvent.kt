package com.k1apps.backgammon.buisness.event

import com.k1apps.backgammon.buisness.Player

data class DiceThrownEvent(val player: Player, val number: Byte)