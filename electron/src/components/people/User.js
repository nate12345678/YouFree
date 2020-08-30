import '../../css/people/User.css';
import React from 'react'
import {
	Button,
	Icon
} from '@material-ui/core';

function User(props) {

	let actionArea;
	switch (props.variant) {
		case 'friends':
			actionArea = null;
			break;
		case 'sent':
			actionArea = (
				<div className='user-request-sent-text'>
					<div>Friend request</div>
					<div>sent</div>
				</div>
			);
			break;
		case 'pending':
			actionArea = (
				<>
					<Button color="primary"
					        variant="outlined"
					        size="small"
					        onClick={props.deleteFriend}
					        disableElevation>
						<Icon>person_remove</Icon>
					</Button>
					<Button className="user-confirm-friend-button"
					        color="primary"
					        variant="outlined"
					        size="small"
					        onClick={props.addFriend}
					        disableElevation>
						<Icon>person_add</Icon>
					</Button>
				</>
			);
			break;
		case 'none':
			actionArea = (
				<Button color="primary"
				        variant="outlined"
				        size="small"
				        onClick={props.addFriend}
				        disableElevation>
					<Icon>person_add</Icon>
				</Button>
			);
			break;
		default:
			actionArea = null;
	}

	return (
		<div className="user-content">
			<div className="user-pic"> </div>
			<div className="user-info">
				<span className="user-username">{props.username}</span>
				<span className="user-email">{props.email}</span>
			</div>
			<div className='user-action-area'>
				{actionArea}
			</div>
		</div>
	);

}

export default User;
