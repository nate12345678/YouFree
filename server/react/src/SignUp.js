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
let inName;
let inPassConfirm;

function handleSubmit(event) {
    event.preventDefault();
    inEmail = event.target.email.value;
    inPass = event.target.password.value;
    inName = event.target.username.value;
    inPassConfirm = event.target.confirm.value;
    console.log(JSON.stringify({'email': inEmail, 'username': inName, 'password': inPass}));
    if(inPass === inPassConfirm) {
        var xhr = new XMLHttpRequest();
        xhr.addEventListener('load', () => {
            console.log(xhr.responseText)
        });
        xhr.open('POST', url + '/user');
        xhr.setRequestHeader('content-type', 'application/JSON' );
        xhr.send(JSON.stringify({'email': inEmail, 'username': inName, 'password': inPass}));
    } else {
        alert("Passwords are not the same!");
    }
}

function SignUp() {
    const classes = useStyles();

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline/>
            <div className={classes.paper}>
                <Typography component="h1" variant="h5">
                    Sign Up
                </Typography>
                <form onSubmit={handleSubmit} className={classes.form} noValidate>
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <TextField
                                size="small"
                                autoComplete="uname"
                                name="userName"
                                variant="outlined"
                                required
                                fullWidth
                                id="username"
                                label="User Name"
                                autoFocus
                            />
                        </Grid>
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
                        <Grid item xs={12}>
                            <TextField
                                size="small"
                                variant="outlined"
                                required
                                fullWidth
                                name="confirm"
                                label="Confirm Password"
                                type="password"
                                id="confirm"
                                autoComplete="current-password"
                            />
                        </Grid>
                    </Grid>
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        className={classes.submit}
                    >
                        Sign Up
                    </Button>
                </form>
            </div>
            <Box mt={5}>
                <Copyright/>
            </Box>
        </Container>
    );
}

export default SignUp;