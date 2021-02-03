import '../../css/people/People.css';
import React from 'react';
import {
	Card,
	CardContent,
	Paper,
	Tab,
	Tabs
} from '@material-ui/core';
import Search from './Search';
import Friends from './Friends';
import Pending from './Pending';
import { connect } from 'react-redux';
import {
	addFriend,
	clearFriendRequestNotifications,
	deleteFriend,
	getFriends,
	getPendingRequests,
	searchUsers
} from '../../state/Effects';


function select(state) {
	return {
		friends: state.friends.items,
		pendingRequests: state.pendingRequests.items,
		searchResults: state.searchResults.items,
	};
}


function mapDispatchToProps(dispatch) {
	return {
		getFriends: () => dispatch(getFriends()),
		addFriend: (user) => dispatch(addFriend(user)),
		deleteFriend: (user) => dispatch(deleteFriend(user)),
		getPending: () => dispatch(getPendingRequests()),
		searchUsers: (query) => dispatch(searchUsers(query)),
		clearNotifications: () => dispatch(clearFriendRequestNotifications())
	};
}


class ConnectedPeoplePage extends React.Component {

	constructor(props) {
		super(props);

		this.state = {
			currentTab: 0
		}
	}


	handleChange = (event, newValue) => {
		this.setState({
			currentTab: newValue
		});
	}


	render() {
		let content;
		switch (this.state.currentTab) {
			case 0:
				content = <Friends getFriends={this.props.getFriends}
				                   friends={this.props.friends}
				/>;
				break;
			case 1:
				content = <Pending getPending={this.props.getPending}
				                   pending={this.props.pendingRequests}
				                   addFriend={this.props.addFriend}
				                   deleteFriend={this.props.deleteFriend}
				                   clearNotifications={this.props.clearNotifications}
				/>
				break;
			default:
				content = <Search searchUsers={this.props.searchUsers}
				                  searchResults={this.props.searchResults}
				                  addFriend={this.props.addFriend}
				                  deleteFriend={this.props.deleteFriend}
				/>
				break;
		}

		return (
			<Card className="people-card" elevation={4}>
				<Paper id="tabs" square elevation={1}>
					<Tabs value={this.state.currentTab}
					      variant="fullWidth"
					      indicatorColor="primary"
					      textColor="primary"
					      onChange={this.handleChange}>

						<Tab label="Friends" />
						<Tab label="Pending" />
						<Tab label="Search" />
					</Tabs>
				</Paper>

				<CardContent className="people-card-content">
					{content}
				</CardContent>
			</Card>
		);
	}
}

const PeoplePage = connect(select, mapDispatchToProps)(ConnectedPeoplePage);
export default PeoplePage;
