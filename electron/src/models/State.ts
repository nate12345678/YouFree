import { User } from './Responses';

export interface AppState {
	token: string;
	self: User;
	theme: string;
	errorMessage: string | object;
	mySchedule: any;
	friendSchedules: any;
	friends: EntityState<any>;
	pendingRequests: EntityState<any>;
	searchResults: EntityState<any>;
	numNotifications: number;
}

export interface EntityState<T> {
	[id: number]: number;
	items: T[];
}
