import '../../css/people/User.css';
import React from 'react'
import {
	Button,
	Icon,
	Typography
} from '@material-ui/core';

function User(props) {

	let actionArea;
	switch (props.variant) {
		case 'FRIENDS':
			actionArea = null;
			break;
		case 'SENT':
			actionArea = (
				<Typography>Request sent</Typography>
			);
			break;
		case 'PENDING':
			actionArea = (
				<>
					<Button className="UserDenyFriendButton"
					        color="primary"
					        variant="outlined"
					        size="small"
					        onClick={props.deleteFriend}
					        disableElevation>
						<Icon>person_remove</Icon>
					</Button>
					<Button className="UserConfirmFriendButton"
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
		case 'NONE':
			actionArea = (
				<Button className="UserAddFriendButton"
				        color="primary"
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
		<div className="UserContent">
			<div className="UserPic"> </div>
			<div className="UserInfo">
				<span className="UserUsername">{props.username}</span>
				<span className="UserEmail">{props.email}</span>
			</div>
			{actionArea}
		</div>
	);

}

export default User;
