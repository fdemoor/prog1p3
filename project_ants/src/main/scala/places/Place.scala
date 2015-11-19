package places


class Place (name: String, posx: Int, posy: Int) {
	
	def x (): Int = posx
	def y (): Int = posy
	
	private var in: Place = this
	private var out: Place = this
	
	def entrance (p: Place): Unit = { in = p }
	def exit (p: Place): Unit = { out = p }
	
	def getHeihgt (): Int = 66
	def getWidth (): Int = 66
	
} 
