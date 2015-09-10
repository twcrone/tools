defmodule WatchNCopy do 

	def run(source, destination, last_change \\ {}) do
		fstat = File.stat!(source)
		ctime = fstat.ctime
		if(ctime != last_change) do
			IO.puts("Copying #{source} to #{destination}...")
			File.cp(source, destination)
		end
		:timer.sleep(5000)
		run(source, destination, ctime)
	end

end

# Usage:  elixir watch_n_copy.exs <source_file_path> <destination_file_path>
[source, destination] = System.argv()
WatchNCopy.run(source, destination)