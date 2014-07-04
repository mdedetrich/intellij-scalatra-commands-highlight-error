package com.example.app

import org.json4s.DefaultFormats
import org.scalatra._
import org.scalatra.commands._
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.validation.{FieldName, ValidationError}
import scalate.ScalateSupport
import org.scalatra.json._
import shapeless.HNil
import scalaz.Scalaz
import Scalaz._
import shapeless._

class SomeCommand extends Command with JsonCommand {
  val field:Field[String] = asType[String]("field").required
  val field2:Field[String] = asType[String]("field2").required

  protected implicit def jsonFormats = DefaultFormats.withBigDecimal
}

case class SomeCommandData(field:String,field2:String)

object SomeCommandData {
  def iso[L <: HList, M <: HList](l: L)
                                 (implicit iso: Generic.Aux[SomeCommandData, M], eq: L =:= M): SomeCommandData = iso.from(l)
}

class MyScalatraServlet extends IntellijscalatracommandshighlighterrorStack with JacksonJsonSupport with JacksonJsonParsing {

  private def construct(c:SomeCommand) = {
    sequence(
      c.field.value.toSuccess(new ValidationError("Some error", Option(FieldName("field")), None, Seq.empty)).toValidationNel ::
      c.field2.value.toSuccess(new ValidationError("Some error", Option(FieldName("field2")), None, Seq.empty)).toValidationNel ::
      HNil
    ).map(x => SomeCommandData.iso(x))
  }

  get("/") {
    val cmd = command[SomeCommand]
    cmd.apply(construct).fold(
      errors => { //Highlighting error here

      },
      data => { //Highlighting error here

      }

    )
  }

  protected implicit def jsonFormats = DefaultFormats.withBigDecimal
}
