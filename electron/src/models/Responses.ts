import { Relationship } from './Relationship';

export interface User {
	id: number;
	username: string;
	email: string;
}

export interface UserSearchResponse {
	user: User;
	relationship: Relationship;
}

export interface RelatedUser extends User {
	relationship: Relationship
}

export interface IntervalResponse {
	id: number;
	userId: number;
	dayOfWeek: number;
	startMin: number;
	endMin: number;
}

export type Schedule = IntervalResponse[][];

export interface NamedSchedule {
	user: User;
	schedule: Schedule;
}

export interface FriendRequestNotification {
	id: number;
	recipientId: number;
	requesterId: number;
}

export interface Frame {
	command: string;
	headers: {
		'content-length': string;
		'content-type': string;
		'message-id': string;
		destination: string;
		subscription: string;
	};
	body: string
}
