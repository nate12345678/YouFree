import '../css/Header.css';
import React from 'react';
import { useHistory } from 'react-router-dom';
import {
	AppBar,
	Button,
	Icon,
	Toolbar,
	Typography
} from '@material-ui/core';

function Header(props) {

	const history = useHistory();


	function invertTheme() {
		const body = document.body;

		if (body.classList.replace('light', 'dark')) {
			return;
		}

		body.classList.replace('dark', 'light');
	}


	return (
		<AppBar id="appBar" position="sticky">
			<Toolbar>
				<Typography id="home" variant="h5" color="inherit">You Free</Typography>
				<Button id="tab-home" variant="contained" color="secondary" onClick={() => history.push('/')}>Home</Button>
				<Button id="tab-search" variant="contained" color="secondary" onClick={() => history.push('/search')}>Add Friends</Button>
				<Icon id="lightModeIcon" color="inherit" onClick={invertTheme}>cloud</Icon>
			</Toolbar>
		</AppBar>
	);
}

export default Header;
