import React from 'react';
import SignUp from "./SignUp";
import SignIn from "./SignIn";
import AppBar from "@material-ui/core/es/AppBar/AppBar";
import Toolbar from "@material-ui/core/es/Toolbar/Toolbar";
import Typography from "@material-ui/core/es/Typography/Typography";
import Button from "@material-ui/core/es/Button/Button";

const url = "https://patrickubelhor.com/api/v1";

class Login extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			in: <SignIn/>,
			up: <SignUp/>,
			page: 0 //0 is in, 1 is up
		};

		this.clickSignIn = this.clickSignIn.bind(this);
		this.clickSignUp = this.clickSignUp.bind(this);
	}

	runLogin(inEmail, inPass) {
		var xhr = new XMLHttpRequest();
		xhr.addEventListener('load', () => {
			console.log(xhr.responseText)
		});
		xhr.open('GET', url + '/login');
		xhr.setRequestHeader('email', inEmail);
		xhr.setRequestHeader('password', inPass.toString());
		xhr.send();
	}

	runCreateUser(inName, inEmail, inPass, inPassConfirm) {
		if(inPass === inPassConfirm) {
			var xhr = new XMLHttpRequest();
			xhr.addEventListener('load', () => {
				console.log(xhr.responseText)
			});
			xhr.open('POST', url + '/user');
			xhr.send(JSON.stringify({'email': inEmail, 'username': inName, 'password': inPass}));
		} else {
			alert("Passwords are not the same!");
		}
	}

	// runHello() {
	//     var xhr = new XMLHttpRequest();
	//     xhr.addEventListener('load', () => {
	//         alert(xhr.responseText)
	//     });
	//     xhr.open('GET', helloUrl);
	//     xhr.send();
	// }

	clickSignIn() {
		this.setState({page: 0});
	}

	clickSignUp() {
		this.setState({page: 1});
	}

	render() {
		let content = null;
		if (this.state.page === 0) {
			content = this.state.in;
		} else if (this.state.page === 1) {
			content = this.state.up;
		}

		return (
			<div>
				<AppBar position="static">
					<Toolbar>
						<Typography style={{flexGrow: 1}} variant="h6">
							You Free
						</Typography>
						<Button color="inherit" onClick={this.clickSignIn}>Sign In</Button>
						<Button color="inherit" onClick={this.clickSignUp}>Sign Up</Button>
					</Toolbar>
				</AppBar>
				{content}
			</div>
		);

	}

}

export default Login;