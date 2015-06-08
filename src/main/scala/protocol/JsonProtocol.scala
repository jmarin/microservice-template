package protocol

import model.Status
import spray.json.DefaultJsonProtocol

trait JsonProtocol extends DefaultJsonProtocol {
  implicit val statusFormat = jsonFormat3(Status.apply)
}