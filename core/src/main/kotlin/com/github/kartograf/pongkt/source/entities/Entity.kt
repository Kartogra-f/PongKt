package com.github.kartograf.pongkt.source.entities

import com.badlogic.gdx.math.Vector2

class Player(dynamicsImpl: Dynamics, playableImpl: Playable) : Drawable, Dynamics by dynamicsImpl, Playable by playableImpl {

    companion object {
        fun new(initialPosition: Vector2, dimensions: Vector2, speed: Vector2): Player {
            return Player(DynamicsImpl(false, initialPosition, dimensions, speed), PlayableImpl())
        }
    }
}

class AI(dynamicsImpl: Dynamics, aiControllerImpl: AIController) : Drawable, Dynamics by dynamicsImpl, AIController by aiControllerImpl {

    companion object {
        fun new(initialPosition: Vector2, dimensions: Vector2, speed: Vector2): AI {
            return AI(DynamicsImpl(false, initialPosition, dimensions, speed), AiControllerImpl())
        }
    }
}

class Ball(dynamicsImpl: Dynamics, collidableImpl: Collidable, ballControlerImpl: BallController) : Drawable, Dynamics by dynamicsImpl, Collidable by collidableImpl, BallController by ballControlerImpl{

    companion object {
        fun new(initialPosition: Vector2, dimensions: Vector2, speed: Vector2): Ball {
            return Ball(DynamicsImpl(true, initialPosition, dimensions, speed), CollidableImpl(), BallControllerImpl())
        }
    }
}