from flask import Flask, render_template, request, redirect, session, flash, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_login import UserMixin, login_user, login_required, logout_user,current_user 
from os import path
from sqlalchemy.sql import func
from flask_login import LoginManager
import json

#from flask_wtf import wtforms
from wtforms import StringField, PasswordField, SubmitField
from wtforms.validators import InputRequired, Length, ValidationError
import bcrypt


def create_database(app):
    if not path.exists('instance/database.db'):
        with app.app_context():
            db.create_all()
        print("Created Database!")


app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///database.db'
app.config['SECRET_KEY'] = '6636249910f03d697e945e662330ce11c45327dfce6e7038'
db = SQLAlchemy(app)
login_check = False
username = ""
create_database(app)
login_manager = LoginManager()
login_manager.init_app(app)




@login_manager.user_loader
def load_user(id):
    return User.query.get(int(id))


if __name__ == "__main__":
    app.run(debug=True)



class User(db.Model, UserMixin):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(10), nullable=False, unique=True)
    password = db.Column(db.String(80), nullable=False)
    task =  db.Column(db.Integer)
    time =  db.Column(db.Integer)


    def __init__(self, username, password,task,time) :
        self.username = username
        self.password = bcrypt.hashpw(password.encode("utf-8"), bcrypt.gensalt()).decode("utf-8")
        self.task = task
        self.time = time

    def check_password(self, password):
        return bcrypt.checkpw(password.encode("utf-8"), self.password.encode("utf-8"))



with app.app_context():
    db.create_all()


def check_password (password):
    password_check = {"character": 0, "number": 0, "symbol": 0}
    for i in password:
            if i.isalpha():
                password_check["character"] += 1
            elif i.isdigit():
                password_check["number"] += 1
            else:
                password_check["symbol"] += 1
    return password_check["character"] == 0 or password_check["number"] == 0 or password_check["symbol"] == 0


@app.route("/progress")
@login_required
def progress():
    return render_template("progress.html",current_user=current_user)

@app.route("/setting")
def setting():
    return render_template("setting.html")


@app.route("/logout")
@login_required
def logout():
    logout_user()
    return redirect("/login")

@app.route("/")
def index():
    return render_template("index.html",user=current_user)
    

@app.route("/login", methods=["GET","POST"])
def login():
    if request.method == 'POST':
        username = request.form["username"].strip()
        password = request.form["password"].strip()
        user = User.query.filter_by(username=username).first()
        if user and user.check_password(password):
            session["username"] = user.username
            session["password"] = user.password
            value = request.form.getlist('check') 
            print(value)
            if  value == [u'edit']:
                login_user(user,remember=True)
            else : 
                login_user(user,remember=False)
            return redirect("/")
        else:
            flash("Your enter invalid user or password if you don't have account please register below",'error')
            return render_template("login.html")
    else :
        return render_template("login.html")


@app.route("/register", methods=["GET","POST"])
def register():
    if request.method == "POST":
        username = request.form["username"].strip()
        password = request.form["password"].strip()
        checkpassword =  request.form["re-password"].strip()
        task = 0
        time = 0
        user = User.query.filter_by(username=username).first()
        if username == "" or password=="" or checkpassword == ""   :
            flash("You should not leave the field empty ",catagory='error')
        elif password != checkpassword :
            flash("Your password don't match",'error')
        elif check_password(password):
            flash("Your password should contain at least one character, number and special characters",'error')
        elif len(username) < 4  :
            flash("You should enter username more than or equal to 3 character",'error')
        elif len(password) < 6:
            flash("You should enter password more than or equal to 6 digits",'error')
        elif len(username) > 10:
            flash("You should enter username less than 10 character",'error')
        elif len(password) > 13:
            flash("You should enter password less than 13 digits",'error')
        elif user:
            flash("Your username is alerady regiestered, Please select enter other username",'error')
        else:
            new_user = User(username=username, password=password, task=task,time=time)
            db.session.add(new_user)
            db.session.commit()
            login_user(new_user,remember=True)
            return redirect('/')
    return render_template("register.html")

@app.route("/addtasknum", methods=["POST"])
@login_required
def addtask_num():
    obj = json.loads(request.data)
    if obj["task"]=="success":
        current_user.task += 1
        db.session.commit()
        return jsonify({})
    
@app.route("/addtimernum", methods=["POST"])
@login_required
def addtimer_num():
    obj = json.loads(request.data)
    current_user.time += int(obj["timer"])
    print(int(obj["timer"]))
    db.session.commit()
    return jsonify({})