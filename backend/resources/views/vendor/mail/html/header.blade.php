@props(['url'])
<tr>
<td class="header">
<a href="{{ $url }}" style="display: inline-block;">
@if (trim($slot) === 'Platea')
<img src="{{asset('platea_logo.png')}}" class="logo" alt="PLatea Logo">
@else
{{ $slot }}
@endif
</a>
</td>
</tr>
