import SockJS from 'sockjs-client';
import Stomp from 'stompjs';


class Notifier {

	stompClient = null;

	constructor() {
		this.stompClient = null;
	}

	connect = () => {
		const socket = new SockJS('https://youfree.patrickubelhor.com/websocket-connect');
		this.stompClient = Stomp.over(socket);
		this.stompClient.connect({}, this.onConnectSuccess, this.onConnectFailure);
	}

	onConnectSuccess = (frame) => {
		console.log('Connected to YouFree websocket!');
		console.log(frame);
		this.subscribeToHello();
	}

	onConnectFailure = () => {
		console.log('Failed to connect again :(');
	}

	disconnect = () => {
		if (this.stompClient !== null) {
			this.stompClient.disconnect();
			console.log('Disconnected websocket');
		}
	}

	subscribeToHello = () => {
		this.stompClient.subscribe('/topic/hello', (frame) => {
			console.log(frame.body);
		});
	}

	sendHello = () => {
		this.stompClient.send('/app/hello', {}, 'Hello YouFree');
	}

}

export default new Notifier();
