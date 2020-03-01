import React from 'react';

class IntervalForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      startMin: 0,
      endMin: 0,
      day: 0
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

    let content = {
      dayOfWeek: this.state.day,
      startMin: this.state.startMin,
      endMin: this.state.endMin
    }

    this.props.submitAction(content);
  }

  render() {
    return (
      <form onSubmit={this.handleSubmit} style={{width:'200px'}}>
        <label>
          startMin:
          <input type='text' name='startMin' value={this.state.startMin} onChange={this.handleChange} />
        </label>
        <br />
        <label>
          endMin:
          <input type='text' name='endMin' value = {this.state.endMin} onChange={this.handleChange} />
        </label>
        <br />
        <label>
          <select name='day' value = {this.state.day} onChange={this.handleChange}>
            <option value={0}>Monday</option>
            <option value={1}>Tuesday</option>
            <option value={2}>Wednesday</option>
            <option value={3}>Thursday</option>
            <option value={4}>Friday</option>
            <option value={5}>Saturday</option>
            <option value={6}>Sunday</option>
          </select>
        </label>
        <input type='submit' value='Submit' />
      </form>
    );
  }
}

export default IntervalForm;