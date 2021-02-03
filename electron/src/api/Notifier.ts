import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { Frame } from '../models/Responses';


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

	onConnectSuccess = (notificationCallback: (frame: Frame) => void) => (frame) => {
		console.log(frame);
		this.subscribeToNotifications(notificationCallback);
	};

	onConnectFailure = () => {
		console.log('Failed to connect again :(');
	};

	disconnect = () => {
		this.client.disconnect();
		console.log('Disconnected websocket');
	};

	subscribeToNotifications = (callback: (frame: Frame) => void) => {
		const headers = {
			token: this.token
		};

		this.client.subscribe(`/app/queue/notifications/${this.userId}`, callback, headers);
	};

}

export default new Notifier();
