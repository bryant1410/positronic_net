package org.positronicnet.test

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

import org.positronicnet.orm.ReflectUtils

class BaseClassForThing {
  val inheritedVar = "inherited"
}

class ThingWithNiladicConstructor extends BaseClassForThing {
  val instanceVar = "initialized"
}

case class ThingWithDefaultingConstructor(
  val x: String = "x",
  val y: Int    = 12345
)

class ReflectUtilsSpec
  extends Spec with ShouldMatchers
{
  describe( "declared fields by name" ) {
    it ("should get immediately declared fields") {
      val klass = classOf[ ThingWithDefaultingConstructor ]
      val fields = ReflectUtils.declaredFieldsByName( klass )
      fields( "x" ) should equal (klass.getDeclaredField( "x" ))
      fields( "y" ) should equal (klass.getDeclaredField( "y" ))
    }
    it ("should get inherited fields") {

      val baseKlass = classOf[ BaseClassForThing ]
      val klass     = classOf[ ThingWithNiladicConstructor ]
      val fields    = ReflectUtils.declaredFieldsByName( klass )

      fields("instanceVar")  should equal(klass.getDeclaredField("instanceVar"))
      fields("inheritedVar") should equal(baseKlass.getDeclaredField("inheritedVar"))
    }
  }

  describe( "get object builder" ) {

    it ("should find niladic constructor") {

      val builder = ReflectUtils.getObjectBuilder[ ThingWithNiladicConstructor ]
      val rec: ThingWithNiladicConstructor = builder()
      
      rec.inheritedVar should equal ("inherited")
      rec.instanceVar  should equal ("initialized")
    }

    it ("should find all-defaults constructor") {

      val builder =
        ReflectUtils.getObjectBuilder[ ThingWithDefaultingConstructor ]
      val rec: ThingWithDefaultingConstructor = builder()

      rec.x should equal ("x")
      rec.y should equal (12345)
    }
  }
}