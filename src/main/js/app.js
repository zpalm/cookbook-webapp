const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {recipes: []};
	}

	componentDidMount() {
		client({method: 'GET', path: '/recipes'}).done(response => {
			this.setState({recipes: response.entity});
		});
	}

	render() {
		return (
			<RecipeList recipes={this.state.recipes}/>
		)
	}

	class RecipeList extends React.Component{
    	render() {
    		const recipes = this.props.recipes.map(recipe =>
    			<Recipe key={recipe.id} recipe={recipe}/>
    		);
    		return (
    			<table>
    				<tbody>
    					<tr>
    						<th>Recipe Name</th>
    						<th>Ingredients</th>
    						<th>Steps</th>
    					</tr>
    					{recipes}
    				</tbody>
    			</table>
    		)
    	}
    }

    class Recipe extends React.Component{
    	render() {
    		return (
    			<tr>
    				<td>{this.props.recipe.name}</td>
    				<td>{this.props.recipe.ingredients}</td>
    				<td>{this.props.recipe.steps}</td>
    			</tr>
    		)
    	}
    }

    ReactDOM.render(
    	<App />,
    	document.getElementById('react')
    )
}