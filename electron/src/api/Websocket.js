import SockJS from 'sockjs';
import Stomp from 'stompjs';


export default class MyWebsocket {

	// socket = new SockJS('/app');
	// stompClient = new Stomp(this.socket);
	stompClient;

	connect = () => {
		const socket = new SockJS('/app');
		this.stompClient = Stomp.over(socket);
		this.stompClient.connect({}, () => console.log('Connected to websocket'), () => console.log('Websocket connection failed'));
	}

}
