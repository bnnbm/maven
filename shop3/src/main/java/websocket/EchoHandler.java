package websocket;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
@Component
public class EchoHandler extends TextWebSocketHandler {
	//접속된 클라이언트 목록
   private Set<WebSocketSession> clients = new HashSet<WebSocketSession>();
   @Override //연결될 경우.  chat.jsp에서 문서를 만들자 마자 세션 하나 할당
   public void afterConnectionEstablished(WebSocketSession session) throws Exception {
      super.afterConnectionEstablished(session);
      System.out.println("클라이언트 접속" + session.getId()); //접속자수 카운트
      clients.add(session);
   }
   
   @Override //클라이언트에서 메세지 수신
   public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
      String loadMessage = (String)message.getPayload();
      //loadMessage : 클라이언트가 전송한 메세지
      System.out.println(session.getId() + ":클라이언트 메세지:" + loadMessage);
      clients.add(session); //중복된 경우 add 불가. 원래는 있으면 안됌
      //접속된 클라이언트들 에게 수신된 메세지 전송
      for(WebSocketSession s : clients) {
         s.sendMessage(new TextMessage(loadMessage));
         
      }
   }
   @Override //오류발생시.
   public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
      super.handleTransportError(session, exception);
      System.out.println("오류발생:" + exception.getMessage());
   }
   @Override //연결 종료
   public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
      super.afterConnectionClosed(session, status);
      System.out.println("클라이언트 접속 해제" + status.getReason());
      clients.remove(session);
   }

}