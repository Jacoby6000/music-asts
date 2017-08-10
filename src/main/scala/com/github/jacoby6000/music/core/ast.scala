package com.github.jacoby6000.music.core

object ast {
  case class KeySignature(note: Duration, beats: Int)

  sealed trait Accidental
  sealed trait Flattie extends Accidental // Until I come up with a better name...
  sealed trait Sharpie extends Accidental // Same here
  case object Flat extends Flattie
  case object Sharp extends Sharpie
  case object Natural extends Flattie with Sharpie with Accidental // All notes can be natural

  sealed trait Pitch
  case class A(accidental: Accidental) extends Pitch
  case class B(accidental: Flattie) extends Pitch
  case class C(accidental: Sharpie) extends Pitch
  case class D(accidental: Accidental) extends Pitch
  case class E(accidental: Flattie) extends Pitch
  case class F(accidental: Sharpie) extends Pitch
  case class G(accidental: Accidental) extends Pitch

  sealed trait FlattiePitchCompanion[T] {
    def apply(accidental: Flattie): T
    def flat: T = apply(Flat)
    def ♭ : T = flat
    def natural: T = apply(Natural)
    def ♮ : T = natural
  }

  sealed trait SharpiePitchCompanion[T] {
    def apply(accidental: Sharpie): T
    def sharp: T = apply(Sharp)
    def ♯ : T = sharp
    def natural: T = apply(Natural)
    def ♮ : T = natural
  }

  sealed trait AccidentalPitchCompanion[T] {
    def apply(accidental: Accidental): T
    def sharp: T = apply(Sharp)
    def ♯ : T = sharp
    def natural: T = apply(Natural)
    def ♮ : T = natural
    def flat: T = apply(Flat)
    def ♭ : T = flat
  }

  object A extends AccidentalPitchCompanion[A]
  object B extends FlattiePitchCompanion[B]
  object C extends SharpiePitchCompanion[C]
  object D extends AccidentalPitchCompanion[D]
  object E extends FlattiePitchCompanion[E]
  object F extends SharpiePitchCompanion[F]
  object G extends AccidentalPitchCompanion[G]

  sealed trait Duration
  sealed trait StandaloneDuration extends Duration
  case object Sixteenth extends StandaloneDuration
  case object Eighth extends StandaloneDuration
  case object Quarter extends StandaloneDuration
  case object Half extends StandaloneDuration
  case object Whole extends StandaloneDuration
  case class Dotted(duration: StandaloneDuration, dots: Int) extends Duration
  case class Fermata(duration: Duration) extends Duration

  sealed trait Note
  case class LeafNote(pitch: Pitch, duration: Duration, octave: Int) extends Note
  case class Tie(left: Note, right: Note) extends Note

  sealed trait Dynamics
  sealed trait Volume extends Dynamics
  case object Fortississimo extends Volume
  case object Fortissimo extends Volume
  case object Forte extends Volume
  case object MezzoForte extends Volume
  case object Mezzo extends Volume
  case object Piano extends Volume
  case object Pianissimo extends Volume
  case object Pianississimo extends Volume
  case class Creshendo(from: Volume, to: Volume) extends Dynamics
  type Decreshendo = Creshendo // Technically the same thing, but people like to use different words.

  sealed trait Segment[T]
  case class Repeated[T](segment: T, times: Int) extends Segment[T]
  case class InKeySignature[T](key: KeySignature, segment: T) extends Segment[T]
  case class WithDynamics[T](dynamics: Dynamics, segment: T) extends Segment[T]
  case class Sequence[T](notes: List[Note]) extends Segment[T]
  case class Slur[T](notes: List[Note]) extends Segment[T]
  case class Together[T](left: T, right: T) extends Segment[T]
  case class And[T](left: T, right: T) extends Segment[T]
  case class Lit[T](value: T) extends Segment[T]
}
