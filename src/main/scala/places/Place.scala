package places

import insects._

class Place (name: String, posx: Int, posy: Int) {
	
	def x (): Int = posx
	def y (): Int = posy
	
	private var in: Place = this
	private var out: Place = this
	
	private var bees_in: List[Insect] = Nil
	private var ant_in: List[Insect] = Nil
	
	def entrance (p: Place): Unit = { in = p }
	def exit (p: Place): Unit = { out = p }
	
	def add_bee (b: Insect): Unit = { bees_in = b::bees_in }
	
	def add_ant (a: Insect): Unit = {
		ant_in match {
			case Nil => ant_in = a::Nil
			case _ => println ("There is already an ant in this place!")
		}
	}
	
	def remove_ant (a: Insect): Unit = {
		ant_in match {
			case Nil => println ("There is no ant to remove!")
			case _ => ant_in = Nil
		}
	}

	def is_ant_in (): Boolean = { ant_in == Nil }
	def get_ant_in (): Insect = { ant_in(0) }
	
} 
