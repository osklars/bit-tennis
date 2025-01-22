package model.physics

import narr.NArray
import slash.matrix.*
import slash.vector.*

case class BallFlightState
(
  x: Matrix[9, 1], // [X, V, A]
  P: Matrix[9, 9] = Matrix.zeros, // State uncertainty covariance
)


case class BallFlightFilter
(
  dt: Double,
  R: Matrix[6, 6],
  Q: Matrix[9, 9],
):
  private val F: Matrix[9, 9] =
    Matrix[9, 9](NArray[Double](
      1, 0, 0, dt, 0, 0, 0.5*dt*dt, 0, 0,
      0, 1, 0, 0, dt, 0, 0, 0.5*dt*dt, 0,
      0, 0, 1, 0, 0, dt, 0, 0, 0.5*dt*dt,
      0, 0, 0, 1, 0, 0, dt, 0, 0,
      0, 0, 0, 0, 1, 0, 0, dt, 0,
      0, 0, 0, 0, 0, 1, 0, 0, dt,
      0, 0, 0, 0, 0, 0, 1, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 1, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 1,
    ))

  private val H: Matrix[6, 9] =
    Matrix[6, 9](NArray[Double](
      1, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 1, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 1, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 1, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 1, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 1, 0, 0, 0,
    ))

  def update(state: BallFlightState, measurement: Vec[6]): BallFlightState = {
    // Predict
    val x_pred = F * state.x
    val P_pred = F * state.P * F.transpose + Q

    // Kalman gain
    val K = P_pred * H.transpose * (H * P_pred * H.transpose + R).inverse

    // Update
    BallFlightState(x_pred + K * (measurement.asColumnMatrix - H * x_pred), (Matrix.identity[9, 9] - K * H) * P_pred)
  }