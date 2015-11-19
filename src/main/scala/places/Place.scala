package places


class Place (name: String, posx: Int, posy: Int) {
	
	def x: Int = posx
	def y: Int = posy
	
	private var in: Place = this
	private var out: Place = this
  
  private var bees: List[Bee] = Nil
  private var ant: Option[Ant] = None
	
	def entrance(p: Place): Unit = { in = p }
	def exit(p: Place): Unit = { out = p }
	
	def getHeihgt: Int = 66
	def getWidth: Int = 66
  
  def isBeesIn : Boolean = bees.isEmpty
  def addBee(b: Bee): Unit = bees = b::bees
  def removeBee(b: Bee): Unit = {
    bees match {
      case Nil => ()
      case h::t =>
        if (b == h) t
          else h::(removeBee(t))
    }
  }
  
  def isAntIn: Boolean = ant == None
  
  def addAnt(a): Unit = {
    assert(ant == None)
    ant = Some(a)
  }
    
  def removeAnt: Unit = {
    assert(ant != None)
    ant = None
  }
  
}
