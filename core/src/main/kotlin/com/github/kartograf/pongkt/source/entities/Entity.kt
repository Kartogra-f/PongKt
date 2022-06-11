package com.github.kartograf.pongkt.source.entities

import com.badlogic.gdx.math.Vector2

class Player(dynamicsImpl: Dynamics ) : Drawable, Dynamics by dynamicsImpl {

    companion object {
        fun new(initialPosition: Vector2, dimensions: Vector2, speed: Vector2): Player {
            return Player(DynamicsPlayerImpl(initialPosition, dimensions, speed))
        }
    }
}

class AI(dynamicsImpl: Dynamics ) : Drawable, Dynamics by dynamicsImpl {

    companion object {
        fun new(initialPosition: Vector2, dimensions: Vector2, speed: Vector2): AI {
            return AI(DynamicsAiImpl(initialPosition, dimensions, speed))
        }
    }
}

class Ball(dynamicsImpl: Dynamics ) : Drawable, Dynamics by dynamicsImpl {

    companion object {
        fun new(initialPosition: Vector2, dimensions: Vector2, speed: Vector2): Ball {
            return Ball(DynamicsBallImpl(initialPosition, dimensions, speed))
        }
    }
}