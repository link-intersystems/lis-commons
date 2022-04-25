{% assign doclist = site.pages | sort: 'url'  %}
    {% for doc in doclist %}
        {% if doc.name contains '.md' or doc.name contains '.html' %}
                - [{{ doc.name }}]({{ site.baseurl }}{{ doc.url }})
        {% endif %}
{% endfor %}

<ul>
    <li><a href="{{ site.baseurl }}/1.3.0-rc2-SNAPSHOT/index.html">1.3.0-rc2-SNAPSHOT</a></li>
</ul>
