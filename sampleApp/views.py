from django.shortcuts import render, get_object_or_404
from django.http import HttpResponseRedirect
from django.views import generic

def index(request):
	context = ''
	return render(request, 'index.html')
