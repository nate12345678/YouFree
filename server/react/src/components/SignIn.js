import React from 'react';

class SignIn extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      email: '',
      password: ''
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChange(event) {
    const target = event.target;
    const name = target.name;
    this.setState({
      [name]: target.value
    });
  }

  handleSubmit(event) {
    event.preventDefault();

    this.props.submitAction(this.state.email, this.state.password);
  }

  render() {
    return (
      <form onSubmit={this.handleSubmit} style={{width:'200px'}}>
        <label>
          Username/Email:
          <input type='text' name='email' value={this.state.email} onChange={this.handleChange} />
        </label>
        <br />
        <label>
          Password:
          <input type='text' name='password' value = {this.state.password} onChange={this.handleChange} />
        </label>
        <br />
        <input type='submit' value='Submit' />
      </form>
    );
  }
}

export default SignIn;