import React from 'react';
import './App.css';
import WeekView from './components/WeekView';
import { Router, Switch, Route } from "react-router-dom";
import history from './history';
import IntervalForm from './components/IntervalForm';
import SignIn from './components/SignIn';
import SignUp from './components/SignUp'
import Login from "./Login";

const url = "https://patrickubelhor.com/api/v1";

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      intervals: null,
      token: 0,
      user: {
        id: 0,
        username: '',
        email: ''
      }
    }
  }

  componentDidMount() {
    // this.login('n@p.com','q');
    this.fetchSchedule();
  }

  fetchSchedule = () => {
    fetch(url + '/schedule/' + this.state.user.id, {
      headers: {
        'token': this.state.token
      }
    })
      .then(response => response.json())
      .then(data => {
        console.log(data);
        this.setState({intervals: data});
      });
  }

  fetchUser = () => {
    fetch(url + '/user/', {
      headers: {
        'token': this.state.token
      }
    })
      .then(response => response.json())
      .then(data => {
        console.log(data);
        this.setState({user: data});
      });
  }

  deleteInterval = (id, event) => {
    let ok = window.confirm("This interval will be deleted. Are you sure?");
    if (ok) {
      fetch(url + '/schedule/' + id, {
        method: 'delete',
        headers: {
          'intervalId': id,
          'token': this.state.token
        }
      })
        .then(response => response.json())
        .then(data => {
          console.log(data);
          this.setState({intervals: data});
        });
    }
  }

  addInterval = (interval) => {
    fetch(url + '/schedule', {
      method: 'put',
      headers: {
        'token': this.state.token,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(interval)
    })
      .then(response => response.json())
      .then(data => {
        console.log(data);
        this.setState({intervals: data});
      });
  }

  login = (email, password) => {
    fetch(url + '/login', {
      headers: {
        'email': email,
        'password': password
      }
    })
        .then(response => {
            if(response.ok) {
                return response.json()
            }
        })
        .then(data => {
            console.log(data);
            this.setState({
                user: data,
                token: data.id});
            this.fetchSchedule();
            history.push('/Home')
        });
  }

  signUp = (user) => {
    fetch(url + '/user', {
      method: 'post',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(user)
    })
      .then(response => response.json())
      .then(data => {
        alert("Created User: " + data.username + " with id " + data.id)
        console.log(data);
      });
  }

  render() {
    let username = null
    username = this.state.user == null ? '' : this.state.user.username;
    
    let content = null;
    content = this.state.intervals == null ? <div/> : <WeekView intervals={this.state.intervals} cellAction={this.deleteInterval} />;
    

    return (
        <div>
            <Router history={history}>
                <Switch>
                    <Route exact path="/">
                        <h2>Sign In</h2>
                        <SignIn submitAction={this.login}/>
                        <h2>Sign Up</h2>
                        <SignUp submitAction={this.signUp}/>
                    </Route>
                    <Route path="/Home">
                      <h2>Logged In: {username}</h2>
                      {content}
                      <h2>Add Interval</h2>
                      <IntervalForm submitAction={this.addInterval}/>
                    </Route>
                </Switch>
            </Router>
        </div>
    );
  }
}

export default App;
