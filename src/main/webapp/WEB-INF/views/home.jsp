<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>

<%@ include file="head.jsp"%>
<%@ include file="nav.jsp"%>

<body>

	<div class="container">
		<h1>Create New Task</h1>
		<form id="task-form" method="post">
			<div class="form-group">
				<label for="title" class="form-label">Title:</label>
				<input type="text" class="form-control" id="title" name="title" required>
			</div>
			<div class="form-group">
				<label for="description" class="form-label">Description:</label>
				<input type="text" class="form-control" id="description" name="description" required>
			</div>
			<div class="form-group">
				<label for="deadline" class="form-label">Deadline:</label>
				<input type="datetime-local" class="form-control" id="deadline" name="deadline" required pattern="\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d{1,9})?" title="Format: yyyy-mm-ddThh:mm:ss[.fffffffff]">
			</div>
			<div class="form-group">
				<button type="submit" class="btn btn-primary" value="Create">Create</button>
			</div>		
		</form>
	</div>

	<c:if test="${not empty tasks}">
	    <div class="container">
	    	<h1>My Tasks</h1>
	        <div class="row" id="task-list">
	            <c:forEach items="${tasks}" var="task">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">${task.title}</h5>
                            <p class="card-text">${task.description}</p>
                            <p class="card-text">Created At <fmt:formatDate value="${task.deadline}" pattern="yyyy-MM-dd HH:mm"/></p>
                            <p class="card-text">${task.completed ? "Completed" : "Incomplete"}</p>
                        </div>
                    </div>
	            </c:forEach>
	        </div>
	    </div>
	</c:if>

	<script>		
		$(function() {
			  $('#task-form').submit(function(event) {
			    event.preventDefault();
			    var form = $(this);
			    $.ajax({
			      type: 'POST',
			      url: "${rootDirectory}/tasks",
			      data: form.serialize(),
			      success: function(result) {
			        form.trigger('reset');
			        updateTaskList();
			      }
			    });
			  });

			  function updateTaskList() {
			    $.ajax({
			      type: 'GET',
			      url: '${rootDirectory}/usertask',
			      success: function(result) {
			    	 $('#task-container').html($(result).find('#task-container').html());
			      }
			    });
			  }
			});
	</script>

</body>

<%@ include file="footer.jsp"%>

</html>
