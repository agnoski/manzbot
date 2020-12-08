package app

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class ActionsService {
  private static ActionsService instance;
  
  private BlockingQueue<Map> queue

  private ActionsService() {
    queue = new LinkedBlockingQueue<Map>();
  }

  public static synchronized ActionsService getInstance() {
    if(instance == null) {
      instance = new ActionsService()
    }
    return instance
  }

  public void put(Map element) {
    queue.put(element)
  }

  public Map take() {
    return queue.take()
  }

  public void printQueue() {
    println("-----Start queue elelments-----")
    this.queue.forEach( { e ->
      println(e)
    })
    println("-----End queue elelments-----")
  }
}