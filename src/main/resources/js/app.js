'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const axios = require('axios');
import autosize from 'autosize';

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {recipes: []};
		this.updateRecipes = this.updateRecipes.bind(this);
		this.addRecipe = this.addRecipe.bind(this);
	}

	componentDidMount() {
		client({method: 'GET', path: '/recipes'}).done(response => {
			this.setState({recipes: response.entity});
		});
	}

	updateRecipes(data) {
        this.setState({recipes: data})
    }

    addRecipe(recipe) {
            let update = this.props.update;
            axios.post('/recipes', {
            name: recipe.name,
            steps: recipe.steps})
                .then(res=>{
                    client({method: 'GET', path: '/invoices'}).done(response => {
                        this.props.update(response.entity);
                    });
                    $.notify("Recipe added", "success");
                    //window.location = "/retrieve" //This line of code will redirect you once the submission is succeed
                })
    }

	render() {
    		return (
    		<div class="container-fluid">
    		    <div class="row">
    		        <AddRecipe addRecipe={this.addRecipe} update={this.updateRecipes}/>
    		    </div>
    		    <div class="row">
    		        <RecipeList recipes={this.state.recipes} update={this.updateRecipes}/>
    		    </div>
    		</div>
    		)
    	}
}

class AddRecipe extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            id: '',
            name: '',
            ingredients: '',
            step: '',
            steps: [{step: ''}],
        };
    }

    componentDidMount(){
           this.textarea.focus();
           autosize(this.textarea);
        }

    handleSubmit(event) {
        event.preventDefault();
        var recipe = {
          id: this.state.id,
          name: this.state.name,
          ingredients: this.state.ingredients,
          steps: this.state.steps
        };
        this.props.addRecipe(recipe);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    /*handleStepChange(event) {
        this.setState({
        step: event.target.value
        });
    }*/
   handleStepChange(event, i) {
        /*const newSteps = this.state.steps.map((item, idx) =>
            {
           const newData = idx === i ? event.target.value : {};

           return {
           ...item,
           ...newData};
        });
        this.setState({newSteps});*/
        const newSteps = this.state.steps.map((recipeStep, idx) => {
              if (i !== idx) return recipeStep;
              return { ...recipeStep, step: event.target.value };
            });

            this.setState({ steps: newSteps });
    }

    /*handleAddStep(event) {
        this.setState({steps: this.state.steps.concat([{step: ""}])
        });
    }*/

    handleAddStep() {
            /*this.setState(state => {
                const steps = state.steps.concat({step: value});
                return {
                    steps,
                    step: '',
                };
            });*/
            this.setState({
                  steps: this.state.steps.concat([{ step: "" }])
                });
            /*const newSteps = this.state.steps.concat([{step: this.state.step}]);
            this.setState({steps: newSteps});*/
            //this.setState({steps: this.state.steps.concat([{step: this.state.step}])})
        }

        handleRemoveStep(i) {
        this.setState({
              steps: this.state.steps.filter((step, idx) => i !== idx)
            });
        }

    render() {
    const style = {
                    maxHeight:'75px',
                    minHeight:'38px',
                      resize:'none',
                      padding:'9px',
                      boxSizing:'border-box',
                      fontSize:'15px'};
            return (
                <React.Fragment>
                    <div class="modal fade" id={"addRecipeModal"} tabindex="-1" role="dialog">
                      <div class="modal-dialog modal-xl" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h5 class="modal-title">Hello</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                            </button>
                          </div>
                          <div class="modal-body">
                          <div class="container-fluid">
                            <form>
                            <div class="row">
                            <div class="col-md-4">
                                <div class="md-form">
                                  <input type="text" id="form1" placeholder="Name" class="form-control" name="name" onChange={event => this.handleChange(event)}/>
                                </div>
                                </div>
                                </div>
                                {this.state.steps.map((recipeStep, i) => (
                                <div class="row">
                                <div class="col-md-6">
                                <div class="input-group mb-3">
                                    <textarea key={i} type="text" placeholder={`Step #${i + 1}`}
                                    class="form-control" aria-describedby="button-addon2" value={recipeStep.step}
                                    name="step" style={style} ref={el => this.textarea = el} rows={1} defaultValue="" onChange={event => this.handleStepChange(event, i)}>
                                    </textarea>
                                    <div class="input-group-append">
                                       <button class="btn btn-outline-secondary" type="button" id="button-addon2" onClick={() => this.handleRemoveStep(i)}>X</button>
                                    </div>
                                    </div>
                                </div>
                                </div>
                                ))}
                                   <button type="button" onClick={() => this.handleAddStep()}>
                                   Add Step
                                   </button>
                            </form>
                            </div>
                          </div>
                          <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary" onClick={event => this.handleSubmit(event)}>Save changes</button>
                          </div>
                        </div>
                      </div>
                    </div>

                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target={"#addRecipeModal"}>
                        Add recipe
                    </button>
                    </React.Fragment>
            )
    }
}

class ShowDetailsButton extends React.Component{
    showIngredientsList() {
        let ingredientsList = [];
        var ingredients = this.props.recipe.ingredients;
        for (var i = 0; i < ingredients.length; i++) {
            var ingredient = ingredients[i];
            ingredientsList.push(
                <li class="list-group-item">
                    {ingredient.quantity} {ingredient.unit} <b>{ingredient.ingredientType.type}</b>
                </li>
            );
        }
        return ingredientsList;
    }

    showRecipeSteps() {
            let recipeSteps = [];
            var steps = this.props.recipe.steps;
            for (var i = 0; i < steps.length; i++) {
                var step = steps[i];
                recipeSteps.push(
                    <li class="list-group-item">
                        <b>{i + 1}</b>. {step.step}
                    </li>
                );
            }
            return recipeSteps;
        }

    render() {
        return (
            <React.Fragment>
                <div class="modal fade" id={"showDetailsModal" + this.props.recipe.id} tabindex="-1" role="dialog">
                  <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 class="modal-title">{this.props.recipe.name}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                          <span aria-hidden="true">&times;</span>
                        </button>
                      </div>
                      <div class="modal-body">
                        <div class="container">
                            <div class="row">
                                <div class="col-5">
                                    <ul class="list-group list-group-flush">
                                        {this.showIngredientsList()}
                                    </ul>
                                </div>
                                <div class="col">
                                    <ul class="list-group list-group-flush">
                                        {this.showRecipeSteps()}
                                    </ul>
                                </div>
                            </div>
                        </div>
                      </div>
                      <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                      </div>
                    </div>
                  </div>
                </div>
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target={"#showDetailsModal" + this.props.recipe.id}>
                    Show recipe
                </button>
            </React.Fragment>
        )
    }
}

class RecipeList extends React.Component{
	render() {
		const recipes = this.props.recipes.map(r =>
			<Recipe key={r.id} recipe={r}/>
		);
		return (
		<div class="col-6">
			<table id="main" class="table table-hover">
				<thead class="thead-dark">
				    <tr>
                        <th>Recipe</th>
                		<th></th>
                	</tr>
				</thead>
				<tbody>
					{recipes}
				</tbody>
			</table>
		</div>
		)
	}
}

class Recipe extends React.Component{
	render() {
		return (
			<tr>
				<td>{this.props.recipe.name}</td>
				<td><ShowDetailsButton recipe={this.props.recipe} /></td>
			</tr>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)
