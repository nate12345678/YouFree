import React from 'react';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import {makeStyles} from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import history from "./history";
// import FormControlLabel from '@material-ui/core/FormControlLabel';
// import Checkbox from '@material-ui/core/Checkbox';

function Copyright() {
	return (
		<Typography variant="body2" color="textSecondary" align="center">
			{'Copyright © '}
			<Link color="inherit" href="http://patrickubelhor.com/swagger-ui.html">
				You Free
			</Link>{' '}
			{new Date().getFullYear()}
			{'.'}
		</Typography>
	);
}

const useStyles = makeStyles(theme => ({
	paper: {
		marginTop: theme.spacing(8),
		display: 'flex',
		flexDirection: 'column',
		alignItems: 'center',
	},
	avatar: {
		margin: theme.spacing(1),
		backgroundColor: theme.palette.secondary.main,
	},
	form: {
		width: '100%', // Fix IE 11 issue.
		marginTop: theme.spacing(3),
	},
	submit: {
		margin: theme.spacing(3, 0, 2),
	},
}));

const url = "https://patrickubelhor.com/api/v1";
let inEmail;
let inPass;

function handleSubmit(event) {
    event.preventDefault();
    inEmail = event.target.email.value;
    inPass = event.target.password.value;
    let xhr = new XMLHttpRequest();
    xhr.addEventListener('load', () => {
        if(xhr.status === 200) {
            history.push('/Home');
        } else {
            alert(xhr.response);
        }
    });
    xhr.open('GET', url + '/login');
    xhr.setRequestHeader('email', inEmail);
    xhr.setRequestHeader('password', inPass.toString());
    xhr.send();
}

function SignIn() {
	const classes = useStyles();

	return (
		<Container component="main" maxWidth="xs">
			<CssBaseline/>
			<div className={classes.paper}>
				<Typography component="h1" variant="h5">
					Sign In
				</Typography>
				<form onSubmit={handleSubmit} className={classes.form} noValidate>
					<Grid container spacing={2}>
						<Grid item xs={12}>
							<TextField
								size="small"
								variant="outlined"
								required
								fullWidth
								id="email"
								label="Email Address"
								name="email"
								autoComplete="email"
								autoFocus
							/>
						</Grid>
						<Grid item xs={12}>
							<TextField
								size="small"
								variant="outlined"
								required
								fullWidth
								name="password"
								label="Password"
								type="password"
								id="password"
								autoComplete="current-password"
							/>
						</Grid>
					</Grid>
					{/*<FormControlLabel*/}
					{/*control={<Checkbox value="remember" color="primary" />}*/}
					{/*label="Remember me"*/}
					{/*/>*/}
					<Button
						type="submit"
						fullWidth
						variant="contained"
						color="primary"
						className={classes.submit}
					>
						Sign In
					</Button>
				</form>
			</div>
			<Box mt={8}>
				<Copyright/>
			</Box>
		</Container>
	);
}

export default SignIn;