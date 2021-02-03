import axios, { AxiosResponse } from 'axios';
import { FriendRequestNotification, NamedSchedule, Schedule, User, UserSearchResponse } from '../models/Responses';

type YoufreeResponse<T> = Promise<AxiosResponse<T>>;

class Youfree {

	client = axios.create({
		baseURL: process.env.REACT_APP_SERVER_URL
	});


	createUser = (email: string, username: string, password: string): YoufreeResponse<User> =>
	{
		return this.client.post('/user', {
			email: email,
			username: username,
			password: password
		});
	};


	login = (email: string, password: string): YoufreeResponse<User> =>
	{
		return this.client.get('/login', {
			headers: {
				email: email,
				password: password
			}
		});
	};


	logout = (token: string): YoufreeResponse<void> =>
	{
		return this.client.get('/logout', {
			headers: {
				token: token
			}
		});
	};


	addInterval = (token: string, dayOfWeek: number, startMin: number, endMin: number): YoufreeResponse<Schedule> =>
	{
		return this.client.put('/schedule', {
			dayOfWeek: dayOfWeek,
			startMin: startMin,
			endMin: endMin
		}, {
			headers: {
				token: token
			}
		});
	};


	updateInterval = (token: string, intervalId: number, dayOfWeek: number, startMin: number, endMin: number): YoufreeResponse<Schedule> =>
	{
		return this.client.put(`/schedule/${intervalId}`, {
			dayOfWeek: dayOfWeek,
			startMin: startMin,
			endMin: endMin
		}, {
			headers: {
				token: token
			}
		});
	}


	deleteInterval = (token: string, intervalId: number): YoufreeResponse<Schedule> =>
	{
		return this.client.delete(`/schedule/${intervalId}`, {
			headers: {
				token: token
			}
		});
	};


	getSchedule = (token: string, userId: number): YoufreeResponse<Schedule> =>
	{
		return this.client.get(`/schedule/${userId}`, {
			headers: {
				token: token
			}
		});
	};


	getFriendSchedules = (token: string): YoufreeResponse<NamedSchedule[]> =>
	{
		return this.client.get('/schedule/friends', {
			headers: {
				token: token
			}
		});
	};


	getFriends = (token: string): YoufreeResponse<User[]> =>
	{
		return this.client.get('/friends', {
			headers: {
				token: token
			}
		});
	};


	addFriend = (token: string, userId: number): YoufreeResponse<void> =>
	{
		return this.client.put(`/friends/${userId}`, {}, {
			headers: {
				token: token
			}
		});
	};


	deleteFriend = (token: string, userId: number): YoufreeResponse<void> =>
	{
		return this.client.delete(`/friends/${userId}`, {
			headers: {
				token: token
			}
		});
	};


	getPending = (token: string): YoufreeResponse<User[]> =>
	{
		return this.client.get('/friends/pending', {
			headers: {
				token: token
			}
		});
	};


	searchUsers = (token: string, query: string): YoufreeResponse<UserSearchResponse[]> =>
	{
		return this.client.get('/search', {
			headers: {
				token: token,
				query: query
			}
		});
	};


	acknowledgeNotifications = (token: string, notificationIds: number[]): YoufreeResponse<FriendRequestNotification[]> =>
	{
		return this.client.delete('/notifications', {
			headers: {
				token: token
			},
			data: notificationIds
		});
	};

}

export default new Youfree();
