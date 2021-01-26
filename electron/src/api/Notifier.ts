import SockJS from 'sockjs-client';
import Stomp from 'stompjs';


class Notifier {

	private client = null;
	private token: string = null;
	private userId: number = null;

	constructor() {
		this.client = null;
	}

	connect = (userId: number, token: string, notificationCallback: (frame) => void) => {
		this.userId = userId;
		this.token = token;
		const socket = new SockJS('https://youfree.patrickubelhor.com/websocket-connect');
		this.client = Stomp.over(socket);
		this.client.connect({}, this.onConnectSuccess(notificationCallback), this.onConnectFailure);
	}

	onConnectSuccess = (notificationCallback: (frame) => void) => (frame) => {
		console.log('Connected to YouFree websocket!');
		console.log(frame);
		this.subscribeToHello();
		this.subscribeToNotifications(notificationCallback)
	}

	onConnectFailure = () => {
		console.log('Failed to connect again :(');
	}

	disconnect = () => {
		this.client.disconnect();
		console.log('Disconnected websocket');
	}

	subscribeToHello = () => {
		this.client.subscribe('/topic/hello', (frame) => {
			console.log(frame.body);
		});
	}

	sendHello = () => {
		this.client.send('/app/hello', {}, 'Hello YouFree');
	}

	subscribeToNotifications = (callback: (frame) => void) => {
		const headers = {
			token: this.token
		};

		this.client.subscribe(`/app/queue/notifications/${this.userId}`, callback, headers);
	}

}

export default new Notifier();
