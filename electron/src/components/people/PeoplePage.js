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
	getFriends,
	getPendingRequests
} from '../../state/Effects';


function select(state) {
	return {
		friends: state.friends,
		pendingRequests: state.pendingRequests
	};
}


function mapDispatchToProps(dispatch) {
	return {
		getFriends: () => dispatch(getFriends()),
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
				content = <Pending token={this.props.token} addFriend={this.props.addFriend} deleteFriend={this.props.deleteFriend} handleError={this.props.handleError}/>
				break;
			default:
				content = <Search token={this.props.token} addFriend={this.props.addFriend} deleteFriend={this.props.deleteFriend} handleError={this.props.handleError}/>
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
