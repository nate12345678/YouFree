import { FriendRequestNotification, User } from './Responses';

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
	notifications: EntityState<FriendRequestNotification>;
}

export interface EntityState<T> {
	[id: number]: number;
	count: number;
	items: T[];
}

export interface Entity {
	id: number;
}

export class EntityCalculator {

	static initEntityState<T extends Entity>(): EntityState<T> {
		return {
			items: [],
			count: 0
		};
	}

	static createEntityState<T extends Entity>(entities: T[]): EntityState<T> {
		const nextState = {
			items: entities,
			count: entities.length
		};

		entities.forEach((entity: T, index: number) => nextState[entity.id] = index);

		return nextState;
	}

	static addEntity<T extends Entity>(state: EntityState<T>, entity: T): EntityState<T> {
		return {
			...state,
			[entity.id]: state.count,
			items: state.items.concat(entity),
			count: state.count + 1
		};
	}

	static addAllEntities<T extends Entity>(state: EntityState<T>, entities: T[]): EntityState<T> {
		const nextState = { ...state };

		const nextItems = state.items.slice();
		entities.forEach((entity: T, index: number) => {
			nextState[entity.id] = state.count + index;
			nextItems.push(entity);
		});

		nextState.items = nextItems;
		nextState.count = nextItems.length;

		return nextState;
	}

	static upsertEntity<T extends Entity>(state: EntityState<T>, entity: T, index: number): EntityState<T> {
		const nextState: EntityState<T> = { ...state };
		const nextItems: T[] = state.items.slice();

		nextItems[index] = entity;
		nextState.items = nextItems;

		return nextState;
	}

	static removeEntity<T extends Entity>(state: EntityState<T>, id: number) {
		let nextState = { ...state };

		// Only remove if ID exists in entity state
		if (state.hasOwnProperty(id)) {
			nextState.items = state.items.filter((user: T, index: number) => index !== state[id]);
			nextState.count = state.count - 1;
			delete nextState[id];
		}

		return nextState;
	}

}
