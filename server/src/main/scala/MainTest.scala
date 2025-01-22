import model.physics.{BallFlightFilter, BallFlightState}
import narr.NArray
import slash.matrix.Matrix
import slash.vector.Vec

object MainTest extends App:
  
  val state = BallFlightState(Matrix[9, 1](NArray[Double](
    -2, 0, 0.2, // strike position 2m behind net towards player A, center of board, slightly above board level.
    3, 0, 0.1, // flying towards table and up slightly
    0, 0, -9.81 // gravity
  )), Matrix.zeros[9, 9])
  val filter = BallFlightFilter(
    0.02, // 50 fps
    Matrix.zeros[6, 6],
    Matrix.zeros[9, 9]
  )
