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
	deleteFriend,
	getFriends,
	getPendingRequests
} from '../../state/Effects';


function select(state) {
	return {
		friends: state.friends.items,
		pendingRequests: state.pendingRequests,
		token: state.token // TODO: remove when possible
	};
}


function mapDispatchToProps(dispatch) {
	return {
		getFriends: () => dispatch(getFriends()),
		addFriend: (user) => dispatch(addFriend(user)),
		deleteFriend: (user) => dispatch(deleteFriend(user)),
		getPending: () => dispatch(getPendingRequests())
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
				content = <Friends getFriends={this.props.getFriends} friends={this.props.friends} />;
				break;
			case 1:
				content = <Pending getPending={this.props.getPending}
				                   pending={this.props.pendingRequests}
				                   addFriend={this.props.addFriend}
				                   deleteFriend={this.props.deleteFriend}
				/>
				break;
			default:
				content = <Search token={this.props.token}
				                  addFriend={this.props.addFriend}
				                  deleteFriend={this.props.deleteFriend}
				                  handleError={this.props.handleError}
				/>
				break;
		}

		return (
			<Card id="people-card" elevation={4}>
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

				<CardContent id="peopleCardContent">
					{content}
				</CardContent>
			</Card>
		);
	}
}

const PeoplePage = connect(select, mapDispatchToProps)(ConnectedPeoplePage);
export default PeoplePage;
