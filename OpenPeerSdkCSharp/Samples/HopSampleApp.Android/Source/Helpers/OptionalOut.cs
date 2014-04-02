using System;
using System.Runtime.Serialization;

namespace OpenPeerSdk
{
	namespace Helpers
	{
		public class OptionalOut<Type>
		{
			public Type Result { get; set; }
		}
	}
}
